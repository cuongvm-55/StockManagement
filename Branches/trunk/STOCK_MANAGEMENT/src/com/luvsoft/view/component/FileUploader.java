package com.luvsoft.view.component;

import org.vaadin.easyuploads.UploadField;

import com.luvsoft.Excel.StockTypeImporter;

@SuppressWarnings("serial")
public class FileUploader extends UploadField{
    /**
     * Instantiate a file uploader to save the uploaded file to a desired path
     * @param uploadToPath
     */
    public FileUploader(){
        this.setDisplayUpload(true);
        this.setStorageMode(StorageMode.MEMORY);
        /*this.setStorageMode(StorageMode.FILE);
        FileFactory fileFactory = new FileFactory() {
            @Override
            public File createFile(String fileName, String mimeType) {
                File f = new File(uploadToPath + fileName);
                return f;
            }
        };
        this.setFileFactory(fileFactory);*/
    }

    @Override
    protected void updateDisplay() {
        // TODO Auto-generated method stub
        System.out.println("File: " + super.getLastFileName());
        StockTypeImporter entityImporter = new StockTypeImporter(this.getContentAsStream());
        entityImporter.process();
    }
}
