package icu.crepus.crepusserver;

import java.io.File;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/blog")
public class BlogController {
    private final String BlogRootPath = System.getProperty("user.dir") + "/resources/public/blog/";

    private final HashSet<String> themeSuffixes = new HashSet<>(Arrays.asList(
            "light", "dark"
    ));
    private final HashSet<String> fileSuffixes = new HashSet<>(Arrays.asList(
            "md", "html"
    ));
    ;

    private BlogTreeData buildBlogTreeData(File dir, String prefix) {
        var res = new BlogTreeData();
        var blogs = new HashSet<String>();

        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isHidden()) {
                    continue;
                }
                if (file.isFile()) {
                    // blog-name.style-type.file-type -> blog-name
                    var fileNameParts = file.getName().split("\\.");
                    StringBuilder blogName = new StringBuilder();
                    for (String fileNamePart : fileNameParts) {
                        Set<String> suffixes = new HashSet<>() {{
                            addAll(fileSuffixes);
                            addAll(themeSuffixes);
                        }};
                        if (suffixes.contains(fileNamePart)) {
                            break;
                        }
                        blogName.append(fileNamePart);
                        blogName.append(".");
                    }

                    blogName.deleteCharAt(blogName.length() - 1);

                    if (!blogs.contains(blogName.toString())) {
                        blogs.add(blogName.toString());

                        // /prefix/parent-path/
                        var blogPath = (file.getParent() + "/" + blogName).substring(prefix.length());
                        res.add(new BlogTreeNode(blogPath, blogName.toString(), false, null));
                    }
                } else {
                    res.add(new BlogTreeNode(file.getPath().substring(prefix.length()), file.getName(), true, buildBlogTreeData(file, prefix)));
                }
            }
        }

        return res;
    }

    @GetMapping("{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {

    }

    @GetMapping("/file-tree/{user}")
    public BlogTreeData getBlogTreeData(@PathVariable("user") String user) {
        return buildBlogTreeData(new File(BlogRootPath + user), BlogRootPath + user);
    }

    @GetMapping("/blog-info/{user}/{path}")
    public BlogInfo getBlogInfo(@PathVariable("user") String user, @PathVariable("path") String path) throws UnsupportedEncodingException {
        String blogPath = URLDecoder.decode(path, StandardCharsets.UTF_8);

        int lastSlashIndex = blogPath.lastIndexOf('/');

        String parentPath = blogPath.substring(0, lastSlashIndex);
        String blogName = blogPath.substring(lastSlashIndex + 1);

        var blogFileInfos = new ArrayList<BlogFileInfo>();

        var parentDir = new File(BlogRootPath + user + parentPath);
        var files = parentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(blogName)) {
                    var fileNameParts = file.getName().split("\\.");
                    var urlPath = parentPath + "/" + file.getName();
                    var fileType = fileNameParts.length >= 2 && fileSuffixes.contains(fileNameParts[fileNameParts.length - 1]) ? fileNameParts[fileNameParts.length - 1] : "unknown";
                    var themeType = fileNameParts.length >= 3 && themeSuffixes.contains(fileNameParts[fileNameParts.length - 2]) ? fileNameParts[fileNameParts.length - 2] : "default";
                    blogFileInfos.add(new BlogFileInfo(urlPath, themeType, fileType));
                }
            }
        }

        return new BlogInfo(blogPath, blogName, blogFileInfos);
    }
}
