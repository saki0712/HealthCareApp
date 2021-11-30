package com.demo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.ListAlbumsRequest;
import com.google.photos.library.v1.proto.ListAlbumsResponse;
import com.google.photos.types.proto.Album;

public class ListAlbumsSupplier implements AlbumsSupplier {
	  private final PhotosLibraryClient client;
	  private final ListAlbumsRequest baseRequest;
	  private Optional<ListAlbumsRequest> request;

	  public ListAlbumsSupplier(PhotosLibraryClient client, ListAlbumsRequest request) {
	    this.client = client;
	    this.baseRequest = request;
	    this.request = Optional.of(this.baseRequest);
	  }

	  @Override
	  public List<Album> get() {
	    if (!request.isPresent()) {
	      return Collections.emptyList();
	    }
	    ListAlbumsResponse response = client.listAlbumsCallable().call(request.get());
	    if (response.getNextPageToken().isEmpty()) {
	      request = Optional.empty();
	    } else {
	      request =
	          Optional.of(request.get().toBuilder().setPageToken(response.getNextPageToken()).build());
	    }
	    return response.getAlbumsList();
	  }

	  @Override
	  public void refresh() {
	    this.request = Optional.of(this.baseRequest);
	  }
}
