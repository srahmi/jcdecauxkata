package fr.sri.service;

import fr.sri.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by srahmi on 01/02/2017.
 */
@Service
public class DeveloperService {

    @Autowired
    DeveloperRepository developerRepository;

}
