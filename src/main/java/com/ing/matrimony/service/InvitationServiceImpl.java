package com.ing.matrimony.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ing.matrimony.dto.ActionResponseDto;
import com.ing.matrimony.dto.ActionrequestDto;
import com.ing.matrimony.dto.InvitationDto;
import com.ing.matrimony.dto.InvitationRequestDto;
import com.ing.matrimony.dto.ViewInvitationDto;
import com.ing.matrimony.entity.Invitation;
import com.ing.matrimony.repository.InvitationRepository;

@Service
public class InvitationServiceImpl implements InvitationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InvitationServiceImpl.class);
	
	
	@Autowired
	private InvitationRepository invitationRepository;
	
	@Override
	public InvitationDto invite(InvitationRequestDto invitationDto) {

		LOGGER.info("In InvitationServiceImpl");
		
		InvitationDto invitationResDto=new InvitationDto();
		
		Invitation invitation=new Invitation();
		invitation.setSenderId(invitationDto.getSenderId());
		invitation.setReceiverId(invitationDto.getRecieverId());
		invitation.setInvitationStatus("PENDING");
		invitationResDto.setMessage("Invitation send");
		invitationResDto.setStatusCode(200);
		
		invitationRepository.save(invitation);
		
		return invitationResDto;
	}

	@Override
	public List<ViewInvitationDto> viewSentInterest(Long senderId) {
		List<Invitation> invitationSent= invitationRepository.findBySenderId(senderId);
		List<ViewInvitationDto> listViewInvitationDto = new ArrayList<>(); 
		ViewInvitationDto viewInvitationDto = new ViewInvitationDto();
		for (Invitation viewInvitationDto2 : invitationSent) {
			viewInvitationDto.setSenderId(viewInvitationDto2.getSenderId());
			viewInvitationDto.setReceiverId(viewInvitationDto2.getReceiverId());
			viewInvitationDto.setInvitationStatus(viewInvitationDto2.getInvitationStatus());
			listViewInvitationDto.add(viewInvitationDto);
		}
		return listViewInvitationDto;
	}

	@Override
	public List<ViewInvitationDto> viewReceivedInterest(Long receiverId) {
		
		List<Invitation> invitationSent= invitationRepository.findByReceiverId(receiverId);
		List<ViewInvitationDto> listViewInvitationDto = new ArrayList<>(); 
		ViewInvitationDto viewInvitationDto = new ViewInvitationDto();
		for (Invitation viewInvitationDto2 : invitationSent) {
			viewInvitationDto.setSenderId(viewInvitationDto2.getSenderId());
			viewInvitationDto.setReceiverId(viewInvitationDto2.getReceiverId());
			viewInvitationDto.setInvitationStatus(viewInvitationDto2.getInvitationStatus());
			listViewInvitationDto.add(viewInvitationDto);
		}
		return listViewInvitationDto;
	}

	@Override
	public ActionResponseDto action(ActionrequestDto actionrequestDto) {
	List<Invitation> invitationRecieved =  invitationRepository.findBySenderId(actionrequestDto.getSenderId());
	System.out.println(actionrequestDto.getSenderId());
	System.out.println(invitationRecieved.size());
	ActionResponseDto actionResponseDto=new ActionResponseDto();
	for (Invitation invitatioCheck : invitationRecieved) {
		System.out.println(invitatioCheck.getReceiverId().equals(actionrequestDto.getRecieverId()));
		if(invitatioCheck.getReceiverId().equals(actionrequestDto.getRecieverId()))
		{
			invitatioCheck.setInvitationStatus(actionrequestDto.getStatus());
			invitationRepository.save(invitatioCheck);
			actionResponseDto.setMessage("Status Updated");
			actionResponseDto.setStatusCode(200);
		}
		
	}
	return actionResponseDto;
	
		
		
	}

}
