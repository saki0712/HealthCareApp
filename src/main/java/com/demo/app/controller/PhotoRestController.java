package com.demo.app.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.PhotosLibraryClientFactory;
import com.demo.ResponseMessage;
import com.google.api.client.util.ArrayMap;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;

@RestController
@RequestMapping("/api")
public class PhotoRestController {
	
	private PhotosLibraryClientFactory mPhotosLibraryClientFactory;
	
	PhotoRestController(@Value("${credential_path}") String CredentialPath) {
		this.mPhotosLibraryClientFactory = new PhotosLibraryClientFactory(CredentialPath);
	}

	//アルバムリスト
	@GetMapping("/getAlbumList")
	public String getAlbumList(Model model) {
		try {

			ResponseMessage result = new ResponseMessage().setData("result", "OK");

			List<Album> albums = this.mPhotosLibraryClientFactory.getAlbums();

			if (albums == null) {
				result.setData("authurl", PhotosLibraryClientFactory.AuthUrl);
				return result.getResponse();
			}
			
			List<Map<String, String>> albumsData = new ArrayList<>();
			for (Album album : albums) {
				Map<String, String> albumData = new ArrayMap<>();
				// アルバム指定で必要
				albumData.put("id", album.getId());
				albumData.put("title", album.getTitle());
				// 以下2つはアルバムカバー写真?(できれば)
				//albumData.put("coverPhotoBaseUrl", album.getCoverPhotoBaseUrl());
				// The cover photo media item id field may be empty
				//albumData.put("coverPhotoMediaItemId", album.getCoverPhotoMediaItemId());
				
				albumsData.add(albumData);
			}
			result.setData("albums", albumsData);
			return result.getResponse();

		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		return new ResponseMessage().setData("result", "NG").getResponse();
	}
	
	//選択されたアルバムの写真
	@PostMapping("/getPhotoUrl")
	public String getPhotoUrl(Model model, @RequestBody String albumId) {
		try {
			ResponseMessage result = new ResponseMessage().setData("result", "OK");
			
			Iterable<MediaItem> photos = this.mPhotosLibraryClientFactory.getPhotos();
			
			if(photos == null) {
				result.setData("authurl", PhotosLibraryClientFactory.AuthUrl);
				return result.getResponse();
			}
			
			List<Map<String, String>> photoData = new ArrayList<>();
			for(MediaItem photo : photos) {
				if(photo.getMimeType().indexOf("video") != -1) continue;
				
				Map<String, String> data = new ArrayMap<>();
				data.put("src", photo.getBaseUrl());
				data.put("type", photo.getMimeType());
				photoData.add(data);
			}
			
			result.setData("photos", photoData);
			return result.getResponse();
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		return new ResponseMessage().setData("result", "NG").getResponse();
	}
}
