package de.aklingauf.qrcodehelper.repository;

import de.aklingauf.qrcodehelper.model.QRRedirect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QRRedirectRepository extends JpaRepository<QRRedirect, Long> {
    List<QRRedirect> findByOwnerId(Long ownerId);

}
