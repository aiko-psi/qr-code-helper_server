package de.aklingauf.qrcodehelper.repository;

import de.aklingauf.qrcodehelper.model.QRCode;
import de.aklingauf.qrcodehelper.model.QRRedirect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
    List<QRCode> findAllByRedirectIdIsNotNull();

    List<QRCode> findAllByRedirectIdIsNull();

}