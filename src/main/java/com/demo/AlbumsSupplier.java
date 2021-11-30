package com.demo;

import java.util.List;
import java.util.function.Supplier;

import com.google.photos.types.proto.Album;

public interface AlbumsSupplier extends Supplier<List<Album>> {
	void refresh();
}
