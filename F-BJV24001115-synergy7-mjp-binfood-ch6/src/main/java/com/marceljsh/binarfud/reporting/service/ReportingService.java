package com.marceljsh.binarfud.reporting.service;

import com.marceljsh.binarfud.reporting.dto.ReceiptResponse;

import java.util.UUID;

public interface ReportingService {

  ReceiptResponse generateReceipt(UUID orderId);

}
