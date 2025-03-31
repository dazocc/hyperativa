package com.dazo.hyperativa.card.batch.repository.rowmapper;

import com.dazo.hyperativa.card.batch.domain.FileDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileRowMapper implements RowMapper<FileDTO> {

    @Override
    public FileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FileDTO.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .status(rs.getString("status"))
                .data(rs.getBytes("data"))
                .dataError(rs.getBytes("data_error"))
                .build();
    }
}
