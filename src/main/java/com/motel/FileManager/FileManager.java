package com.motel.FileManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
@Service
public class  FileManager {
    @Autowired
    ServletContext app;
    private Path GetPath(String folder ,String filename){
        File dir = Paths.get(app.getRealPath("/files/"), folder).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    return Paths.get(dir.getAbsolutePath(), filename);
    }
    public byte[] read(String folder , String fileneme){
        Path path = GetPath(folder, fileneme);
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
    public void delete(String folder , String fileneme){
        Path path =GetPath(folder, fileneme);
        path.toFile().delete();
    }
    public List<String> list(String folder ){
        List<String> list = new ArrayList<String>();
        File dir =Paths.get(app.getRealPath("/files/"),folder).toFile();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for(File li : files){
                list.add(li.getName());
                System.out.println(li.getName());
            }
        }
        return list;
    }
    public List<String> save (String folder ,MultipartFile[] files ){
        List<String> list =new ArrayList<String>();
        for(MultipartFile file : files){
            String name = System.currentTimeMillis()+file.getOriginalFilename();
            String ofname = Integer.toHexString(name.hashCode())+name.substring(name.lastIndexOf("."));
            Path path = GetPath(folder, ofname);
           try {
            file.transferTo(path);
            list.add(ofname);
           } catch (Exception e) {
            e.printStackTrace();
           }
        }
        return list;


    }
}
