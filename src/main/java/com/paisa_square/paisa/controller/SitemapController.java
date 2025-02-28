package com.paisa_square.paisa.controller;
import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.service.Advertiseservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/sitemap.xml")
public class SitemapController {

    @Autowired
    private Advertiseservice adService; // Fetch ads from database

    @GetMapping(produces = "application/xml")
    public ResponseEntity<String> getSitemap() {
        List<Advertise> ads = adService.findAlladvertisement(); // Fetch ads from DB
        String sitemap = generateSitemap(ads);

        // Save to file
        saveSitemapToFile(sitemap);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(sitemap);
    }

    private String generateSitemap(List<Advertise> ads) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Add homepage
        xml.append("  <url>\n");
        xml.append("    <loc>https://paisquare.com/</loc>\n");
        xml.append("    <lastmod>").append(LocalDate.now()).append("</lastmod>\n");
        xml.append("    <priority>1.0</priority>\n");
        xml.append("  </url>\n");

        // Add dynamic ad URLs
        for (Advertise ad : ads) {
            xml.append("  <url>\n");
            xml.append("    <loc>https://paisquare.com/advertisements/")
                    .append(ad.getId()) // Add ID before the slug
                    .append("/")
                    .append(ad.getSlug())
                    .append("</loc>\n");;
            xml.append("    <lastmod>")
                    .append(ad.getLastupdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .append("</lastmod>\n");
            xml.append("    <priority>0.7</priority>\n");
            xml.append("  </url>\n");
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private void saveSitemapToFile(String sitemap) {
        try {
            // Define the file path (inside src/main/resources/static/)
            String filePath = Paths.get("src/main/resources/static/sitemap.xml").toAbsolutePath().toString();
            File file = new File(filePath);

            // Create directories if they don't exist
            file.getParentFile().mkdirs();

            // Write the sitemap content to the file
            FileWriter writer = new FileWriter(file);
            writer.write(sitemap);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
