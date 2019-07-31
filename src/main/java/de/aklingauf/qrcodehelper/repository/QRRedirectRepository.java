package de.aklingauf.qrcodehelper.repository;

import de.aklingauf.qrcodehelper.model.QRRedirect;
import de.aklingauf.qrcodehelper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QRRedirectRepository extends JpaRepository<QRRedirect, Long> {
    List<QRRedirect> findByOwner(User owner);

}
