package com.example.hrautomationbackend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class S3Service {

    public static void qwerty(File file) throws IOException {

        AWSCredentials credentials = null;
        try {
            credentials = new EnvironmentVariableCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.", e);
        }
        (Logger.getLogger(ProductService.class.getName())).info(
                "! Creds: " + credentials);
        AmazonS3 s3 = null;
        try{
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "ru-central1"))
                .build();}
        catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot build s3 creds: " + credentials, e);
        }

        String bucketName = "hr-automation";
        String key = file.getName();

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon S3");
        System.out.println("===========================================\n");

        try {
            // Create a list of ETag objects. You retrieve ETags for each object part uploaded,
            // then, after each individual part has been uploaded, pass the list of ETags to
            // the request to complete the upload.
//            List<PartETag> partETags = new ArrayList<PartETag>();
//
//            // Initiate the multipart upload.
//            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, key);
//            InitiateMultipartUploadResult initResponse = s3.initiateMultipartUpload(initRequest);
//
//            // Upload the file parts.
//            long filePosition = 0;
//            long contentLength = file.getSize();
//
//            long partSize = 5 * 1024 * 1024; // Set part size to 5 MB.
//            for (int i = 1; filePosition < contentLength; i++) {
//                // Because the last part could be less than 5 MB, adjust the part size as needed.
//                partSize = Math.min(partSize, (contentLength - filePosition));
//
//                File file1 = createSampleFile();
//                file.transferTo(file1);
//                // Create the request to upload a part.
//                UploadPartRequest uploadRequest = new UploadPartRequest()
//                        .withBucketName(bucketName)
//                        .withKey(key)
//                        .withUploadId(initResponse.getUploadId())
//                        .withPartNumber(i)
//                        .withFileOffset(filePosition)
//                        .withFile(file1)
//                        .withPartSize(partSize);
//
//                // Upload the part and add the response's ETag to our list.
//                UploadPartResult uploadResult = s3.uploadPart(uploadRequest);
//                partETags.add(uploadResult.getPartETag());
//
//                filePosition += partSize;
//            }
//
//            // Complete the multipart upload.
//            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName,
//                    initResponse.getUploadId(), partETags);
//            s3Client.completeMultipartUpload(compRequest);
//
//            /*
//             * Upload an object to your bucket - You can easily upload a file to
//             * S3, or upload directly an InputStream if you know the length of
//             * the data in the stream. You can also specify your own metadata
//             * when uploading to S3, which allows you set a variety of options
//             * like content-type and content-encoding, plus additional metadata
//             * specific to your applications.
//             */

            System.out.println("Uploading a new object to S3 from a file\n");
            s3.putObject(new PutObjectRequest(bucketName, key, file));

            /*
             * Download an object - When you download an object, you get all of
             * the object's metadata and a stream from which to read the contents.
             * It's important to read the contents of the stream as quickly as
             * possibly since the data is streamed directly from Amazon S3 and your
             * network connection will remain open until you read all the data or
             * close the input stream.
             *
             * GetObjectRequest also supports several other options, including
             * conditional downloading of objects based on modification times,
             * ETags, and selectively downloading a range of an object.
             */
//            System.out.println("Downloading an object");
//            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
//            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
//            displayTextInputStream(object.getObjectContent());

            /*
             * List objects in your bucket by prefix - There are many options for
             * listing the objects in your bucket.  Keep in mind that buckets with
             * many objects might truncate their results when listing their objects,
             * so be sure to check if the returned object listing is truncated, and
             * use the AmazonS3.listNextBatchOfObjects(...) operation to retrieve
             * additional results.
             */
//            System.out.println("Listing objects");
//            ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
//                    .withBucketName(bucketName)
//                    .withPrefix("My"));
//            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//                System.out.println(" - " + objectSummary.getKey() + "  " +
//                        "(size = " + objectSummary.getSize() + ")");
//            }
//            System.out.println();

            /*
             * Delete an object - Unless versioning has been turned on for your bucket,
             * there is no way to undelete an object, so use caution when deleting objects.
             */
//            System.out.println("Deleting an object\n");
//            s3.deleteObject(bucketName, key);

            /*
             * Delete a bucket - A bucket must be completely empty before it can be
             * deleted, so remember to delete any objects from your buckets before
             * you try to delete them.
             */
//            System.out.println("Deleting bucket " + bucketName + "\n");
//            s3.deleteBucket(bucketName);
//        } catch (AmazonServiceException ase) {
//            System.out.println("Caught an AmazonServiceException, which means your request made it "
//                    + "to Amazon S3, but was rejected with an error response for some reason.");
//            System.out.println("Error Message:    " + ase.getMessage());
//            System.out.println("HTTP Status Code: " + ase.getStatusCode());
//            System.out.println("AWS Error Code:   " + ase.getErrorCode());
//            System.out.println("Error Type:       " + ase.getErrorType());
//            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());

        }
    }

    public static File createSampleFile(MultipartFile multipartFile) throws IOException {
        String prefix = multipartFile.getOriginalFilename() + "-temp-pic-";
        File file = File.createTempFile(prefix, ".jpg");
        file.deleteOnExit();
        return file;
    }

//    /**
//     * Displays the contents of the specified input stream as text.
//     *
//     * @param input
//     *            The input stream to display as text.
//     *
//     * @throws IOException
//     */
//    private static void displayTextInputStream(InputStream input) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//        while (true) {
//            String line = reader.readLine();
//            if (line == null) break;
//
//            System.out.println("    " + line);
//        }
//        System.out.println();
//    }

}
