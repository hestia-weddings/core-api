package com.hestia.api.domain.payment.service;

import com.hestia.api.common.dto.PageResponse;
import com.hestia.api.common.exception.ResourceNotFoundException;
import com.hestia.api.common.mapper.PageMapper;
import com.hestia.api.domain.payment.dto.CreateWalletRequest;
import com.hestia.api.domain.payment.dto.UpdateWalletRequest;
import com.hestia.api.domain.payment.dto.WalletResponse;
import com.hestia.api.domain.payment.entity.Wallet;
import com.hestia.api.domain.payment.exception.DuplicateWalletException;
import com.hestia.api.domain.payment.mapper.WalletMapper;
import com.hestia.api.domain.payment.repository.WalletRepository;
import com.hestia.api.domain.wedding.entity.Wedding;
import com.hestia.api.domain.wedding.repository.WeddingRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final WeddingRepository weddingRepository;

    @Transactional(readOnly = true)
    public PageResponse<WalletResponse> getWallets(Pageable pageable) {
        Page<Wallet> page = walletRepository.findAll(pageable);
        return PageMapper.toResponse(page.map(walletMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public WalletResponse getWallet(UUID weddingId) {
        Wallet wallet = walletRepository
                .findFirstByWeddingIdAndIsActiveTrue(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        return walletMapper.toResponse(wallet);
    }

    @Transactional(readOnly = true)
    public WalletResponse getWalletById(UUID id) {
        Wallet wallet = walletRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        return walletMapper.toResponse(wallet);
    }

    public WalletResponse createWallet(UUID weddingId, CreateWalletRequest request) {
        Wedding wedding = weddingRepository
                .findByIdAndIsActiveTrue(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wedding not found"));

        if (walletRepository.existsByWeddingIdAndIsActiveTrue(weddingId)) {
            throw new DuplicateWalletException("Wallet already exists for this wedding");
        }

        Wallet wallet = Wallet.builder()
                .pixKey(request.getPixKey())
                .balance(0)
                .fee(500)
                .wedding(wedding)
                .build();

        return walletMapper.toResponse(walletRepository.save(wallet));
    }

    public WalletResponse updateWalletByWedding(UUID weddingId, UpdateWalletRequest request) {
        Wallet wallet = walletRepository
                .findFirstByWeddingIdAndIsActiveTrue(weddingId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        wallet.setPixKey(request.getPixKey());
        return walletMapper.toResponse(walletRepository.save(wallet));
    }

    public WalletResponse updateWalletById(UUID id, UpdateWalletRequest request) {
        Wallet wallet = walletRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        wallet.setPixKey(request.getPixKey());
        return walletMapper.toResponse(walletRepository.save(wallet));
    }
}
