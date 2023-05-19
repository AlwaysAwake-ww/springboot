package com.example.demo.controller;

import com.example.demo.domain.ImageDomain;
import lombok.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Getter
@Setter
@Builder
public class ImageHandler {


    private final List<ImageDomain> imageDomainList;

    private String tempDirectory;
    private String realDirectory;


    public void saveRealPath(){

        File newDir = new File(this.realDirectory);
        if(!newDir.exists()) {
            try {
                newDir.mkdirs();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        for(ImageDomain image : this.imageDomainList){


            if(image.getNewName().startsWith("thumbnail-")){
                thumbnailDelete(this.realDirectory);
            }


            Path tempPath = Paths.get(this.tempDirectory, image.getNewName());
            Path newPath = Paths.get(this.realDirectory, image.getNewName());

            try{
                Files.move(tempPath, newPath, StandardCopyOption.REPLACE_EXISTING);
            }

            catch(IOException e){
                e.getStackTrace();
            }
        }
    }

    private void thumbnailDelete(String directory){

        File dir = new File(directory);

        if(dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            for (File existFile : files) {
                System.out.println("## exist file :: "+existFile.getName());

                if (existFile.isFile() && existFile.getName().startsWith("thumbnail-")) {
                    System.out.println("## exist file checked :: "+existFile.getName());
                    existFile.delete();
                }
            }
        }
    }




    public void deleteImage(){


        File dir = new File(this.realDirectory);
        if(dir.exists()) {

            File[] fileList =  dir.listFiles();
            if(fileList!=null){
                for(File file: fileList){
                    file.delete();
                }
            }
            dir.delete();
        }
    }

    public void copyTempPath(){

        File newDir = new File(this.tempDirectory);
        if(!newDir.exists()) {
            try {
                newDir.mkdirs();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        for(ImageDomain image : this.imageDomainList){

            Path tempPath = Paths.get(this.tempDirectory, image.getNewName());
            Path originPath = Paths.get(this.realDirectory, image.getNewName());

            try{
                Files.copy(originPath, tempPath, StandardCopyOption.REPLACE_EXISTING);
            }

            catch(IOException e){
                e.getStackTrace();
            }
        }

    }
}
