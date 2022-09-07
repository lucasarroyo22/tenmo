package services;

import model.Transfer;
import model.TransferClientDTO;
import model.TransferDTO;
import model.TransferIdDTO;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TenmoService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    private String username = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAuth() {
        return !(this.authToken == null);
    }

    public String[] getUsernames() {
        String[] usernames = null;

        try {
            ResponseEntity<String[]> response = restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), String[].class);
            usernames = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Unable to get username list: " + e.getMessage());
        }

        return usernames;
    }

    public List<TransferClientDTO> getTransfers() {
        List<TransferClientDTO> transfers = new ArrayList<>();

        try {
            ResponseEntity<TransferClientDTO[]> response = restTemplate.exchange(API_BASE_URL + "transfers", HttpMethod.GET, makeAuthEntity(), TransferClientDTO[].class);
            TransferClientDTO[] transfersArr = response.getBody();
            Collections.addAll(transfers, transfersArr);
        } catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
            System.out.println("Unable to get transfers: " + e.getMessage());
        }

        return transfers;
    }

    public List<TransferClientDTO> getTransfersFiltered(String status) {
        List<TransferClientDTO> filteredTransfers = new ArrayList<>();
        try {
            ResponseEntity<TransferClientDTO[]> response = restTemplate.exchange(API_BASE_URL + "transfers?status=" + status, HttpMethod.GET, makeAuthEntity(), TransferClientDTO[].class);
            TransferClientDTO[] transfers = response.getBody();
            for (TransferClientDTO transfer : transfers) {
                if (transfer.getUsernameFrom().equals(username)) {
                    filteredTransfers.add(transfer);
                }
            }
        } catch (RestClientResponseException | ResourceAccessException | NullPointerException e) {
            System.out.println("Unable to get transfers: " + e.getMessage());
        }
        return filteredTransfers;
    }

    public TransferClientDTO sendMoney(String username, BigDecimal amount) {
        return sendOrReceive(username, amount, "send");
    }

    public TransferClientDTO requestMoney(String username, BigDecimal amount) {
        return sendOrReceive(username, amount, "request");
    }

    public TransferClientDTO sendOrReceive(String username, BigDecimal amount, String transferType) {
        TransferClientDTO returnedTransfer = new TransferClientDTO();

        try {
            returnedTransfer = restTemplate.postForObject(
                    API_BASE_URL + transferType,
                    makeTransferEntity(username, amount),
                    TransferClientDTO.class
            );
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Unable to " + transferType + " money: " + e.getMessage());
        }
        return returnedTransfer;
    }

    public boolean acceptTransfer(int transferId) {
        boolean success = false;
        try {
            restTemplate.put(
                    API_BASE_URL + "transfers/approve",
                    makeTransferIdEntity(transferId)
            );
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Unable to approve transfer: " + e.getMessage());
        }
        return success;
    }

    public boolean rejectTransfer(int transferId) {
        boolean success = false;
        try {
            restTemplate.put(
                    API_BASE_URL + "transfers/reject",
                    makeTransferIdEntity(transferId)
            );
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Unable to reject transfer: " + e.getMessage());
        }
        return success;
    }

    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "account", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Unable to get balance: " + e.getMessage());
        }
        return balance;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferDTO> makeTransferEntity(String username, BigDecimal amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        TransferDTO transferDTO = new TransferDTO(username, amount);
        return new HttpEntity<>(transferDTO, headers);
    }

    private HttpEntity<TransferIdDTO> makeTransferIdEntity(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        TransferIdDTO transferIdDTO = new TransferIdDTO(transferId);
        return new HttpEntity<>(transferIdDTO, headers);
    }
}
