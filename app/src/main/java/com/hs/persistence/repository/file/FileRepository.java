package com.hs.persistence.repository.file;


import com.hs.persistence.entity.file.File;
import com.hs.persistence.entity.file.FileReferrer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByReferrerIdAndFileReferrer(Long referrerId, FileReferrer fileReferrer);

}
