package org.example;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *  Crpt api Класс для работы с API Честного знака с ограничением частоты запросов.
 */
public class CrptApi {
    private static final Logger logger = LoggerFactory.getLogger(CrptApi.class);
    private final RateLimiter rateLimiter;
    private final Gson gson = new Gson();
    private static final String API_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";

    /**
     * Instantiates a new Crpt api.
     *
     * @param requestLimit -ограничение запросов
     * @param timeUnit     -промежуток времени – секунда, минута и пр.
     */
    public CrptApi(int requestLimit, TimeUnit timeUnit) {

        double requestPerSecond = timeUnit.toSeconds(1) / requestLimit;

        this.rateLimiter = RateLimiter.create(requestPerSecond);
    }

    /**
     * Create document.
     *
     * @param document  -создаваемый документ
     * @param signature -подпись документа
     */
    public synchronized void createDocument(Document document, String signature) {
        rateLimiter.acquire();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(API_URL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + signature);

            String json = gson.toJson(document);
            httpPost.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                if (statusCode == 200) {
                    logger.info("Document created successfully");
                } else {
                    logger.error("Failed to create document. Status code: {}", statusCode);
                }
            }
        } catch (IOException e) {
            logger.error("Error while creating document", e);
        }

    }
}

/**
 * The type Document.
 */
class Document {
    private Description description;
    private String doc_id;
    private String doc_status;
    private String doc_type;
    private boolean importRequest;
    private String owner_inn;
    private String participant_inn;
    private String producer_inn;
    private String production_date;
    private String production_type;
    private Product[] products;
    private String reg_date;
    private String reg_number;

    /**
     * Gets description.
     *
     * @return the description
     */
    public Description getDescription() {
        return description;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public String getDoc_status() {
        return doc_status;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public boolean isImportRequest() {
        return importRequest;
    }

    public String getOwner_inn() {
        return owner_inn;
    }

    public String getParticipant_inn() {
        return participant_inn;
    }

    public String getProducer_inn() {
        return producer_inn;
    }

    public String getProduction_date() {
        return production_date;
    }

    public String getProduction_type() {
        return production_type;
    }

    public Product[] getProducts() {
        return products;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getReg_number() {
        return reg_number;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public void setDoc_status(String doc_status) {
        this.doc_status = doc_status;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public void setImportRequest(boolean importRequest) {
        this.importRequest = importRequest;
    }

    public void setOwner_inn(String owner_inn) {
        this.owner_inn = owner_inn;
    }

    public void setParticipant_inn(String participant_inn) {
        this.participant_inn = participant_inn;
    }

    public void setProducer_inn(String producer_inn) {
        this.producer_inn = producer_inn;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
    }

    public void setProduction_type(String production_type) {
        this.production_type = production_type;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    /**
     * The type Description.
     */
    public  class Description {
        private String participantInn;

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        public String getParticipantInn() {
            return participantInn;
        }
    }

    /**
     * The type Product.
     */
    public  class Product {
        private String certificate_document;
        private String certificate_document_date;
        private String certificate_document_number;
        private String owner_inn;
        private String producer_inn;
        private String production_date;
        private String tnved_code;
        private String uit_code;
        private String uitu_code;

        public String getCertificate_document() {
            return certificate_document;
        }

        public String getCertificate_document_date() {
            return certificate_document_date;
        }

        public String getCertificate_document_number() {
            return certificate_document_number;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public String getProduction_date() {
            return production_date;
        }

        public String getTnved_code() {
            return tnved_code;
        }

        public String getUit_code() {
            return uit_code;
        }

        public String getUitu_code() {
            return uitu_code;
        }


    }
}

