package cn.tf.project.repository;

import cn.tf.project.entity.Sys_EcaFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Sys_EcaFiles表操作接口
 */
@Repository
public interface EcaFileRepository extends JpaRepository<Sys_EcaFiles, Long> {
    // 根据文件Guid查询附件信息
    @Query(value = "select * from sys_ecafiles f where f.row_guid = :fileGuid", nativeQuery = true)
    Sys_EcaFiles findByFileGuid(@Param("fileGuid") String fileGuid);

    // 根据文件Guid修改文件真实名称
    @Transactional
    @Modifying
    @Query(value = "update sys_ecafiles f set f.actualname = :actualName where f.row_guid = :fileGuid", nativeQuery = true)
    int updateActualNameByFileGuid(@Param("fileGuid") String fileGuid,
                                   @Param("actualName") String actualName);
}
