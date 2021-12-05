package com.example.brick.service;

import com.example.brick.dto.GetCheck01.Response.GetCheck01;
import com.example.brick.dto.GetCheck01.Response.GetCheck01Dto;
import com.opencsv.CSVWriter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class GeneralServiceImpl implements GeneralService {
    @Override
    public Object getCheck01() {
        GetCheck01Dto response = new GetCheck01Dto();
        List<GetCheck01> data = new ArrayList<>();

        String url = "https://www.tokopedia.com/p/handphone-tablet/handphone";

        try {
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("div.css-bk6tzz.e1nlzfl3");

            log.info("{}", elements.size());

            for(Element element : elements){
                GetCheck01 item = new GetCheck01();

                String nameOfProduct = getNameOfProduct(element);;
                String urlOfProduct = getUrlOfProduct(element);
                String priceOfProduct = getPriceOfProduct(element);
                String imageOfProduct = getImageOfProduct(element);
                String ratingOfProduct = getRatingOfProduct(element);
                String nameOfStoreMerchant = getNameOfStoreMerchant(element);
                String description = getDescription(element);

                item.setNameOfProduct(nameOfProduct);
                item.setUrlOfProduct(urlOfProduct);
                item.setPriceOfProduct(priceOfProduct);
                item.setImageOfProduct(imageOfProduct);
                item.setRatingOfProduct(ratingOfProduct);
                item.setNameOfStoreMerchant(nameOfStoreMerchant);
                item.setDescription(description);

                data.add(item);
            }

            response.setData(data);

            List<String[]> stringList = exportToOneLine(data);
            exportData(stringList);
        }catch(Exception e){
            log.info("{}", e);
        }

        return response;
    }

    private void exportData(List<String[]> stringList) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("d:\\monitor.csv"))) {
            writer.writeAll(stringList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<String[]> exportToOneLine(List<GetCheck01> data) {
        List<String[]> result = new ArrayList<>();
        String[] header = {"Name of product", "description", "image", "price", "rating", "name of store merchant"};
        result.add(header);
        final String SEPARATOR = ",";
        for(GetCheck01 item : data){
            String strNameOfProduct =  item.getNameOfProduct();
            String strDescription = (item.getDescription() == null ? "" : item.getDescription());
            String strImageOfProduct = item.getImageOfProduct();
            String strPriceOfProduct= item.getPriceOfProduct();
            String strRatingOfProduct = item.getRatingOfProduct();
            String strNameOfStoreMerchant =  item.getNameOfStoreMerchant();
            String[] oneLine = new String[]{strNameOfProduct, strDescription, strImageOfProduct, strPriceOfProduct,strRatingOfProduct, strNameOfStoreMerchant};
            result.add(oneLine);
        }

        return result;
    }

    private String getDescription(Element element) {
        return null;
    }

    private String getNameOfStoreMerchant(Element element) {
        Element nameOfStoreMerchantElement = element.select("div.css-16vw0vn > div.css-11s9vse > div.css-tpww51 > div.css-vbihp9 > span.css-1kr22w3").last();
        return nameOfStoreMerchantElement.text();
    }

    private String getRatingOfProduct(Element element) {
        Element ratingElement = element.select("div.css-16vw0vn > div.css-11s9vse > div.css-153qjw7 > div > span").first();
        String rating = (ratingElement == null) ? "0" : StringUtils.substringBetween(ratingElement.text(),"(",")");

        log.info("{}", rating);

        return rating;
    }

    private String getImageOfProduct(Element element) {
        Element imageElement = element.select("div.css-16vw0vn > div.css-79elbk > div.css-1c0vu8l > div.css-1bsuur > img").first();
        return imageElement.attr("src");
    }

    private String getPriceOfProduct(Element element) {
        Element priceElement = element.select("div.css-16vw0vn > div.css-11s9vse > div > div.css-4u82jy > span.css-o5uqvq").first();
//        log.info("price elemnt : {}", priceElement);
        return priceElement.text();

    }

    private String getUrlOfProduct(Element element) {
        Element linkElement = element.getElementsByTag("a").first();
        return linkElement.attr("href");
    }

    private String getNameOfProduct(Element element) {
        return element.getElementsByClass("css-1bjwylw").text();
    }
}
