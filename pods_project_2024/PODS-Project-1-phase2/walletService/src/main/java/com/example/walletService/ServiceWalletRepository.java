package com.example.walletService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ServiceWalletRepository extends JpaRepository<ServiceWallet,Integer> {

    @Transactional
   @Modifying
    @Query(value ="UPDATE SERVICE_WALLET  SET SERVICE_WALLET.balance =?1 WHERE SERVICE_WALLET.user_id =?2",nativeQuery = true)
    void updateBalance(@Param("balance") Integer balance,@Param("user_id") Integer user_id);

}
