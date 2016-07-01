package com.cx.szsh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cx.szsh.models.BaseQueryParams;
import com.cx.szsh.models.Video;
import com.cx.szsh.repository.VideoRepo;

@Service
public class LiveVideoService extends BaseService {
	@Autowired
	private VideoRepo repository;
	
	public Page<Video> getVideoList(BaseQueryParams bps) {
		Pageable pageable = new PageRequest(bps.getOffset(), bps.getLimit());
		return repository.findAll(pageable);
	}

	// @Cacheable(value="cxszshVideoCache", key="#id")
	public Video getVideoDetail(String id) {
		logger.info("miss cache:" + id);
		return repository.findOne(id);
	}
	
	public Video addVideo(Video video){
		return repository.save(video);
	}
	
	// @CacheEvict(value="cxszshVideoCache", key="#id")
	public boolean modifyVideo(String id, Video newVideo) {
		if (repository.exists(id)){
			newVideo.setId(id);
			repository.save(newVideo);
			return true;
		}else{
			return false;
		}
	}
	
	// @CacheEvict(value="cxszshVideoCache", key="#id")
	public void deleteVideo(String id) {
		repository.delete(id);
	}
}
