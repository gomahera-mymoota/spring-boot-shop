package com.kosa.shop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Log
@Getter
@Setter
@NoArgsConstructor
public class ShopFile {

    private String savedFileName;

    public ShopFile(String originalFileName) {
        pullSavedFileName(originalFileName);
    }

    private void pullSavedFileName(String originalFileName) {
        if (StringUtils.hasLength(originalFileName)) {
            var uuid = UUID.randomUUID();
            var extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            this.savedFileName = uuid.toString() + extension;
        }
    }

    public void upload(String uploadPath, byte[] fileData) throws Exception {
        var fileUploadFullUrl = uploadPath + "/" + savedFileName;
        var fos = new FileOutputStream(fileUploadFullUrl);

        fos.write(fileData);
        fos.close();
    }
    public void upload(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        pullSavedFileName(originalFileName);
        upload(uploadPath, fileData);
    }

    public void delete(String filePath) throws Exception {
        var deleteFile = new File(filePath);

        if (deleteFile.delete()) {
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
