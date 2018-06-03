package cz.czechitas.java2.dailyplanet;

import java.text.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import javax.servlet.http.*;

import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

@Controller
public class HlavniController {

    private ClanekRepository linkNaData = new ClanekRepository();

    @RequestMapping("/")
    public ModelAndView zobrazIndex() {
        ModelAndView drzakNaData;
        drzakNaData = new ModelAndView("redirect:/clanky/");
        return drzakNaData;
    }

    @RequestMapping("/clanky/")
    public ModelAndView zobrazSeznam() {
        List<Clanek> seznamClanku =  linkNaData.findAll();
        ModelAndView drzakNaData;

        drzakNaData = new ModelAndView("clanky/index");
        drzakNaData.addObject("seznam", seznamClanku);

        return drzakNaData;
    }

    @RequestMapping(value = "/clanky/{cislo}.html", method = RequestMethod.GET)
    public ModelAndView zobrazClanek(@PathVariable("cislo") Long id) {
        ModelAndView drzakNaData;
        drzakNaData = new ModelAndView("clanky/detail");
        Clanek nalezenyClanek = linkNaData.findOne(id);
        drzakNaData.addObject("clanek", nalezenyClanek);
        return drzakNaData;
    }

    @RequestMapping(value = "/clanky/{cislo}.html", method = RequestMethod.POST)
    public ModelAndView zpracujDetail(@PathVariable("cislo") Long id, DetailForm detailClanek) {
        Clanek aktualniClanek = linkNaData.findOne(id);
        aktualniClanek.setNazev(detailClanek.getNazev());
        aktualniClanek.setAutor(detailClanek.getAutor());
        aktualniClanek.setDatum(detailClanek.getDatum());
        linkNaData.save(aktualniClanek);

        return new ModelAndView("redirect:/clanky/");
    }


    @RequestMapping(value = "/clanky/{cislo}.html",
            method = {RequestMethod.POST, RequestMethod.DELETE},
            params = "_method=delete")
    public ModelAndView odstranClanek(@PathVariable("cislo") Long id) {
        linkNaData.delete(id);
        return new ModelAndView("redirect:/clanky/");
    }

    @RequestMapping(value = "/clanky/new.html", method = RequestMethod.GET)
    public ModelAndView zobrazNovyClanek() {
        ModelAndView drzakNaData;
        drzakNaData = new ModelAndView("clanky/detail");
        Clanek novyClanek = new Clanek();
        drzakNaData.addObject("clanek", novyClanek);
        return drzakNaData;
    }

    @RequestMapping(value = "/clanky/new.html", method = RequestMethod.POST)
    public ModelAndView zpracujNovyClanek(DetailForm detailClanek) {
        Clanek novyClanek = new Clanek();
        novyClanek.setNazev(detailClanek.getNazev());
        novyClanek.setAutor(detailClanek.getAutor());
        novyClanek.setDatum(detailClanek.getDatum());
        linkNaData.save(novyClanek);

        return new ModelAndView("redirect:/clanky/");

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NotFoundException extends RuntimeException {}

    

}
