package com.hs.application.board.dto;

import com.hs.persistence.entity.file.File;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
public class BoardPostDetailResponseDto {

    private Long id;
    private String title;
    private String textContent;

    @Setter
    private String writerNickname;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private File[] file;

    public static class File {
        private long id;
        private String fileName;

        public File(com.hs.persistence.entity.file.File file) {
            this.id = file.getId();
            this.fileName = file.getFileName();
        }

    }

    public BoardPostDetailResponseDto(Long id, String title, String textContent, String writerNickname, LocalDateTime createdDate, LocalDateTime modifiedDate, File[] file) {
        this.id = id;
        this.title = title;
        this.textContent = textContent;
        this.writerNickname = writerNickname;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.file = file;
    }

    public void setFiles(com.hs.persistence.entity.file.File file) {
        this.file = new File[]{new File(file)};
    }

    public void setFiles(Collection<com.hs.persistence.entity.file.File> files) {
        this.file = files.stream().map(File::new).toArray(File[]::new);
    }
}
