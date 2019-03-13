import com.github.learningtour786.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class BlogServiceIT {

    @Test
    public void demoITTest() {
        System.out.println("demoITTest running...");
    }

    @Ignore
    public void run() {
        System.out.println("main blog service impl integration testing is running...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("192.168.99.100", 50051)
                .usePlaintext()
                .build();

        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = Blog.newBuilder()
                .setAuthorId("Stephane")
                .setTitle("New blog!")
                .setContent("Hello world this is my first blog!")
                .build();

        CreateBlogResponse createResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build()
        );

        System.out.println("Received create blog response");
        System.out.println(createResponse.toString());

        String blogId = createResponse.getBlog().getId();

        System.out.println("Reading blog....");

        ReadBlogResponse readBlogResponse = blogClient.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build());

        System.out.println(readBlogResponse.toString());

        // trigger a not found error
//        System.out.println("Reading blog with non existing id....");
//
//        ReadBlogResponse readBlogResponseNotFound = blogClient.readBlog(ReadBlogRequest.newBuilder()
//                .setBlogId("5bd83b5cf476074e2bff3495")
//                .build());

        Blog newBlog = Blog.newBuilder()
                .setId(blogId)
                .setAuthorId("Changed Author")
                .setTitle("New blog (updated)!")
                .setContent("Hello world this is my first blog! I've added some more content")
                .build();

        System.out.println("Updating blog...");
        UpdateBlogResponse updateBlogResponse = blogClient.updateBlog(
                UpdateBlogRequest.newBuilder().setBlog(newBlog).build());

        System.out.println("Updated blog");
        System.out.println(updateBlogResponse.toString());

        System.out.println("Deleting blog");
        DeleteBlogResponse deleteBlogResponse = blogClient.deleteBlog(
                DeleteBlogRequest.newBuilder().setBlogId(blogId).build()
        );

        System.out.println("Deleted blog");


//        System.out.println("Reading blog....");
//        // this one should return NOT_FOUND
//        ReadBlogResponse readBlogResponseAfterDeletion = blogClient.readBlog(ReadBlogRequest.newBuilder()
//                .setBlogId(blogId)
//                .build());

        // we list the blogs in our database
        blogClient.listBlog(ListBlogRequest.newBuilder().build()).forEachRemaining(
                listBlogResponse -> System.out.println(listBlogResponse.getBlog().toString())
        );

    }

    @Test
    public void createBlogTest() {
        System.out.println("main blog service impl integration testing is running...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("192.168.99.100", 50051)
                .usePlaintext()
                .build();

        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = Blog.newBuilder()
                .setAuthorId("Jayaprakash")
                .setTitle("New blog!")
                .setContent("Hello world this is my first blog!")
                .build();

        CreateBlogResponse createResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build()
        );

        System.out.println("Received create blog response");
        System.out.println(createResponse.toString());

        String blogId = createResponse.getBlog().getId();

        System.out.println("Reading blog....");

        ReadBlogResponse readBlogResponse = blogClient.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build());

        System.out.println(readBlogResponse.toString());

        Assert.assertEquals(readBlogResponse.getBlog().getAuthorId(),"Jayaprakash");

    }
}
