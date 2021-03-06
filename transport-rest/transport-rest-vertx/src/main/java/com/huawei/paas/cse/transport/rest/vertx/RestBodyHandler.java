/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.paas.cse.transport.rest.vertx;

import java.io.File;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.Response.Status;

import com.huawei.paas.cse.core.exception.CommonExceptionData;
import com.huawei.paas.cse.core.exception.ExceptionFactory;

import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.impl.BodyHandlerImpl;
import io.vertx.ext.web.impl.FileUploadImpl;

/**
 * copy from io.vertx.ext.web.handler.impl.BodyHandlerImpl
 * and modified.
 *
 * allowed to disable fileupload by setUploadsDirectory(null)
 *
 * @author
 * @version  [版本号, 2017年5月2日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RestBodyHandler implements BodyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BodyHandlerImpl.class);

    private static final String BODY_HANDLED = "__body-handled";

    private long bodyLimit = DEFAULT_BODY_LIMIT;

    private String uploadsDir;

    private boolean mergeFormAttributes = DEFAULT_MERGE_FORM_ATTRIBUTES;

    private boolean deleteUploadedFilesOnEnd = DEFAULT_DELETE_UPLOADED_FILES_ON_END;

    public RestBodyHandler() {
        setUploadsDirectory(DEFAULT_UPLOADS_DIRECTORY);
    }

    public RestBodyHandler(String uploadDirectory) {
        setUploadsDirectory(uploadDirectory);
    }

    @Override
    public void handle(RoutingContext context) {
        HttpServerRequest request = context.request();
        // we need to keep state since we can be called again on reroute
        Boolean handled = context.get(BODY_HANDLED);
        if (handled == null || !handled) {
            BHandler handler = new BHandler(context);
            request.handler(handler);
            request.endHandler(v -> handler.end());
            context.put(BODY_HANDLED, true);
        } else {
            // on reroute we need to re-merge the form params if that was desired
            if (mergeFormAttributes && request.isExpectMultipart()) {
                request.params().addAll(request.formAttributes());
            }

            context.next();
        }
    }

    @Override
    public BodyHandler setBodyLimit(long bodyLimit) {
        this.bodyLimit = bodyLimit;
        return this;
    }

    @Override
    public BodyHandler setUploadsDirectory(String uploadsDirectory) {
        this.uploadsDir = uploadsDirectory;
        return this;
    }

    @Override
    public BodyHandler setMergeFormAttributes(boolean mergeFormAttributes) {
        this.mergeFormAttributes = mergeFormAttributes;
        return this;
    }

    @Override
    public BodyHandler setDeleteUploadedFilesOnEnd(boolean deleteUploadedFilesOnEnd) {
        this.deleteUploadedFilesOnEnd = deleteUploadedFilesOnEnd;
        return this;
    }

    private class BHandler implements Handler<Buffer> {

        private RoutingContext context;

        private Buffer body = Buffer.buffer();

        private boolean failed;

        private AtomicInteger uploadCount = new AtomicInteger();

        private boolean ended;

        private long uploadSize = 0L;

        private final boolean isMultipart;

        private final boolean isUrlEncoded;

        BHandler(RoutingContext context) {
            this.context = context;
            Set<FileUpload> fileUploads = context.fileUploads();

            final String contentType = context.request().getHeader(HttpHeaders.CONTENT_TYPE);
            isMultipart = contentType != null && contentType.contains("multipart/form-data");
            isUrlEncoded = contentType != null && contentType.contains("application/x-www-form-urlencoded");

            if (isMultipart || isUrlEncoded) {
                makeUploadDir(context.vertx().fileSystem());
                context.request().setExpectMultipart(true);
                context.request().uploadHandler(upload -> {
                    // *** cse begin ***
                    if (uploadsDir == null) {
                        failed = true;
                        CommonExceptionData data = new CommonExceptionData("not support file upload.");
                        throw new ErrorDataDecoderException(ExceptionFactory.createConsumerException(data));
                    }
                    // *** cse end ***

                    // we actually upload to a file with a generated filename
                    uploadCount.incrementAndGet();
                    String uploadedFileName = new File(uploadsDir, UUID.randomUUID().toString()).getPath();
                    upload.streamToFileSystem(uploadedFileName);
                    FileUploadImpl fileUpload = new FileUploadImpl(uploadedFileName, upload);
                    fileUploads.add(fileUpload);
                    upload.exceptionHandler(context::fail);
                    upload.endHandler(v -> uploadEnded());
                });
            }
            context.request().exceptionHandler(context::fail);
        }

        private void makeUploadDir(FileSystem fileSystem) {
            // *** cse begin ***
            if (uploadsDir == null) {
                return;
            }
            // *** cse end ***

            if (!fileSystem.existsBlocking(uploadsDir)) {
                fileSystem.mkdirsBlocking(uploadsDir);
            }
        }

        @Override
        public void handle(Buffer buff) {
            if (failed) {
                return;
            }
            uploadSize += buff.length();
            if (bodyLimit != -1 && uploadSize > bodyLimit) {
                failed = true;
                context.fail(Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode());
            } else {
                // multipart requests will not end up in the request body
                // url encoded should also not, however jQuery by default
                // post in urlencoded even if the payload is something else
                if (!isMultipart /* && !isUrlEncoded */) {
                    body.appendBuffer(buff);
                }
            }
        }

        void uploadEnded() {
            int count = uploadCount.decrementAndGet();
            // only if parsing is done and count is 0 then all files have been processed
            if (ended && count == 0) {
                doEnd();
            }
        }

        void end() {
            // this marks the end of body parsing, calling doEnd should
            // only be possible from this moment onwards
            ended = true;

            // only if parsing is done and count is 0 then all files have been processed
            if (uploadCount.get() == 0) {
                doEnd();
            }
        }

        void doEnd() {
            if (deleteUploadedFilesOnEnd) {
                if (failed) {
                    deleteFileUploads();
                } else {
                    context.addBodyEndHandler(x -> deleteFileUploads());
                }
            }

            if (failed) {
                return;
            }

            HttpServerRequest req = context.request();
            if (mergeFormAttributes && req.isExpectMultipart()) {
                req.params().addAll(req.formAttributes());
            }
            context.setBody(body);
            context.next();
        }

        private void deleteFileUploads() {
            for (FileUpload fileUpload : context.fileUploads()) {
                FileSystem fileSystem = context.vertx().fileSystem();
                String uploadedFileName = fileUpload.uploadedFileName();
                fileSystem.exists(uploadedFileName, existResult -> {
                    if (existResult.failed()) {
                        LOGGER.warn("Could not detect if uploaded file exists, not deleting: " + uploadedFileName,
                                existResult.cause());
                    } else if (existResult.result()) {
                        fileSystem.delete(uploadedFileName, deleteResult -> {
                            if (deleteResult.failed()) {
                                LOGGER.warn("Delete of uploaded file failed: " + uploadedFileName,
                                        deleteResult.cause());
                            }
                        });
                    }
                });
            }
        }
    }

}
