package com.dazo.hyperativa.card.batch.repository.impl;

import com.dazo.hyperativa.card.batch.domain.FileDTO;
import com.dazo.hyperativa.card.batch.repository.FileRepository;
import com.dazo.hyperativa.card.batch.repository.rowmapper.FileRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepositoryImpl implements FileRepository {

    private static final String SELECT_FILE_PENDING_LIMIT_1 = """
            select id,
                   name,
                   status,
                   data,
                   data_error
            from files
            where status = 'PENDING'
            limit 1""";

    private static final String SELECT_FILE_BY_ID = """
            select id,
                   name,
                   status,
                   data,
                   data_error
            from files
            where id = ?""";

    private static final String UPDATE_FILE_BY_ID = """
            update files
            set status = ?,
                data_error = ?
            where id = ?""";

    private static final String INSERT_FILE = """
            insert into files (id, name, status, data)
            values (?, ?, ?, ?)""";

    private static final String DELETE_ALL_FILES = "delete from files";

    private final JdbcTemplate jdbcTemplate;

    public FileRepositoryImpl(@Qualifier("appJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public FileDTO findFirstFilePending() {
        try {
            return jdbcTemplate.queryForObject(SELECT_FILE_PENDING_LIMIT_1, new FileRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public FileDTO findFileById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_FILE_BY_ID, new FileRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateFileById(Long id, String status, byte[] dataError) {
        jdbcTemplate.update(UPDATE_FILE_BY_ID, status, dataError, id);
    }

    @Override
    public void insertFile(FileDTO filePending) {
        jdbcTemplate.update(INSERT_FILE, filePending.getId(), filePending.getName(), filePending.getStatus(), filePending.getData());
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_FILES);
    }

}
