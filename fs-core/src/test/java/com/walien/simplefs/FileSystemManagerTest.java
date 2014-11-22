package com.walien.simplefs;

import com.walien.simplefs.decorators.TreeDecorator;
import com.walien.simplefs.domain.impl.Directory;
import com.walien.simplefs.domain.impl.File;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSystemManagerTest {

    private FileSystemManager buildFs() {

        Directory root = (Directory) new Directory().setName("/");

        // Dirs
        Directory home = (Directory) new Directory().setName("home").setParent(root);
        Directory etc = (Directory) new Directory().setName("etc").setParent(root);
        Directory usr = (Directory) new Directory().setName("usr").setParent(root);
        Directory share = (Directory) new Directory().setName("share").setParent(root);
        Directory var = (Directory) new Directory().setName("var").setParent(root);
        Directory lib = (Directory) new Directory().setName("lib").setParent(var);
        Directory log = (Directory) new Directory().setName("log").setParent(var);

        // Files
        File file = (File) new File().setContent("TEST").setParent(log).setName("fs.log");

        // L1
        root.addChildNode(home);
        root.addChildNode(etc);
        root.addChildNode(usr);
        root.addChildNode(var);

        // L2
        var.addChildNode(lib);
        var.addChildNode(log);
        usr.addChildNode(share);

        // L3
        log.addChildNode(file);

        return FileSystemManager.getInstance().setRootDir(root);
    }

    @Test
    public void should_init_properly_fs() {

        FileSystemManager fs = buildFs();
        assertThat(fs.pwd().getName()).isEqualTo("/");
    }

    @Test
    public void should_properly_list_of_files() {

        FileSystemManager fs = buildFs();

        assertThat(fs.ls("/")).isNotNull().isNotEmpty().hasSize(4);
        assertThat(fs.ls("/var")).isNotNull().isNotEmpty().hasSize(2);
        assertThat(fs.ls("/usr")).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void should_properly_print_tree() {

        FileSystemManager fs = buildFs();

        String tree = new TreeDecorator().decorate(fs.getRootDir());
        assertThat(tree).isEqualTo("(DIR) - /\n" +
                "\t(DIR) - home/\n" +
                "\t(DIR) - var/\n" +
                "\t\t(DIR) - lib/\n" +
                "\t\t(DIR) - log/\n" +
                "\t\t\t(FIL) - fs.log\n" +
                "\t(DIR) - etc/\n" +
                "\t(DIR) - usr/\n" +
                "\t\t(DIR) - share/\n");
    }

    @Test
    public void should_properly_create_file() {

        FileSystemManager fs = buildFs();

        fs.touch("/var/lib/test");

        assertThat(fs.ls("/var/lib")).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void should_properly_create_and_cat_file() {

        FileSystemManager fs = buildFs();

        fs.touch("/var/lib/test");

        File file = fs.getFile("/var/lib/test");
        file.setContent("TEST");

        assertThat(fs.cat("/var/lib/test")).isEqualTo("TEST");
    }

}