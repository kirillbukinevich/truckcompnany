package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.WriteOffAct;
import com.truckcompany.service.dto.WriteOffActDTO;

import java.time.ZonedDateTime;

public class ManagedWriteOffVM extends WriteOffActDTO{

    public ManagedWriteOffVM(){}

   public ManagedWriteOffVM(WriteOffActDTO writeOff){
       super(writeOff);
   }

   public ManagedWriteOffVM(WriteOffAct writeOffAct){
       super(writeOffAct);
   }
}
