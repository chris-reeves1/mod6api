package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{

    
}