package cz.czechitas.java2.dailyplanet;

import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;
import org.mariadb.jdbc.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;

public class ClanekRepository {

    private JdbcTemplate pokladacDotazu;
    private RowMapper<Clanek> prevodnikClankuZDb;

    public ClanekRepository() {

        try {

            MariaDbDataSource konfiguraceDatabaze = new MariaDbDataSource();
            konfiguraceDatabaze.setUserName("student");
            konfiguraceDatabaze.setPassword("password");
            konfiguraceDatabaze.setUrl("jdbc:mysql://localhost:3306/DailyPlanet_muffin");

            pokladacDotazu = new JdbcTemplate(konfiguraceDatabaze);
            prevodnikClankuZDb = BeanPropertyRowMapper.newInstance(Clanek.class);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);

        }

    }

    public synchronized List<Clanek> findAll() {
        List<Clanek> seznamClanku = pokladacDotazu.query(
                "SELECT ID, Nazev, Autor, Datum FROM clanky", prevodnikClankuZDb);
        return seznamClanku;
    }

    public Clanek findOne(Long id) {
        Clanek clanek = pokladacDotazu.queryForObject(
                "SELECT ID, Nazev, Autor, Datum FROM clanky WHERE id=?", prevodnikClankuZDb, id);
        return clanek;
    }

         
    public Clanek save(Clanek clanekKUlozeniNeboPridani) {
        if (clanekKUlozeniNeboPridani.getId()== null) {
            Clanek clanekKPridani = pridej(clanekKUlozeniNeboPridani);
            return clanekKPridani;
        }  else {
            Clanek clanekKUlozeni = updatuj(clanekKUlozeniNeboPridani);
            return clanekKUlozeni;
        }
    }

    public void delete(Long id) {
        pokladacDotazu.update(
                "DELETE FROM Clanky WHERE ID = ?",
                id);
    }

    private Clanek clone(Clanek clanek) {
        Clanek klonClanku = new Clanek();
        klonClanku.setId(clanek.getId());
        klonClanku.setNazev(clanek.getNazev());
        klonClanku.setAutor(clanek.getAutor());
        klonClanku.setDatum(clanek.getDatum());
        return klonClanku;
    }


    private Clanek pridej(Clanek zaznamKPridani) {
        Clanek clanek = clone(zaznamKPridani);
        GeneratedKeyHolder drzakNaVygenerovanyKlic = new GeneratedKeyHolder();
        String sql = "INSERT INTO clanky (Nazev, Autor, Datum) " +
                "VALUES (?, ?, ?)";
        pokladacDotazu.update((Connection con) -> {
                    PreparedStatement prikaz = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    prikaz.setString(1, clanek.getNazev());
                    prikaz.setString(2, clanek.getAutor());
                    prikaz.setDate(3, Date.valueOf(clanek.getDatum()));
                    return prikaz;
                },
                drzakNaVygenerovanyKlic);
        clanek.setId(drzakNaVygenerovanyKlic.getKey().longValue());
        return clanek;
    }

    private Clanek updatuj(Clanek zaznamKUlozeni) {
        Clanek clanek = clone(zaznamKUlozeni);
        pokladacDotazu.update(
                "UPDATE clanky SET Nazev = ?, Autor = ?, Datum = ? WHERE id = ?",
                clanek.getNazev(),
                clanek.getAutor(),
                clanek.getDatum(),
                clanek.getId());
        return clanek;
    }
}
