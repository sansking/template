package com.git.template.general.code.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 用该类表示一个源码文件,包括以下信息:
 *  绝对路径,文件名,包名,文件内容
 */
public class CodeFile implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String absolutePath;
    private String fileName;
    private String packageName;
    private String content;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getFileName() {
        if(fileName!=null) return fileName;
        else{
            if(absolutePath == null || absolutePath.isEmpty()){
                throw new RuntimeException("文件路径为空");
            }else{
                this.fileName = new File(absolutePath).getName();
                return getFileName();
            }
        }
        
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CodeFile(String absolutePath, String packageName, String content) {
        this.absolutePath = absolutePath;
        this.packageName = packageName;
        this.content = content;
    }

    public CodeFile() {
    }

    @Override
    public String toString() {
        return "CodeFile [absolutePath=" + absolutePath + ", content=" + content + ", fileName=" + fileName
                + ", packageName=" + packageName + "]";
    }

    

}