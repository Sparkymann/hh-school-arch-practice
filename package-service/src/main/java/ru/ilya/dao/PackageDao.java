package ru.ilya.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ilya.model.Package;

@Repository
public interface PackageDao extends JpaRepository<Package, Long> {
}
