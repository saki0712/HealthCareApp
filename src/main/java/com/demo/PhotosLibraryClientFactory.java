package com.demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.proto.ListAlbumsRequest;
import com.google.photos.library.v1.proto.ListAlbumsResponse;
import com.google.photos.library.v1.proto.SearchMediaItemsRequest;
import com.google.photos.library.v1.proto.SearchMediaItemsRequest.Builder;
import com.google.photos.library.v1.proto.SearchMediaItemsResponse;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;



public class PhotosLibraryClientFactory {

	  // com.demo/credentialsのファイル作成
   private static final java.io.File DATA_STORE_DIR =
		   new java.io.File(PhotosLibraryClientFactory.class.getResource("/").getPath(), "credentials");
   private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
   private static final int LOCAL_RECEIVER_PORT = 8081;

	public static PhotosLibraryClient Client;
	// ユーザーに手動で認証してもらうURL
	public static String AuthUrl;
	// 認証情報の取得でダウンロードした認証ファイルのパス
	public String CredentialsPath;

	public PhotosLibraryClientFactory(String credentialsPath) {
		this.CredentialsPath = credentialsPath;
	}

	/** Creates a new {@link PhotosLibraryClient} instance with credentials and scopes. */
	public boolean init() throws IOException, GeneralSecurityException {
		if (Client != null)
			return true;

		Credentials credentials = getUserCredentials();
		if (credentials == null)
			return false;

		PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
				.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();

		Client = PhotosLibraryClient.initialize(settings);
		return true;
	}

	// 認証情報を取得する処理
	private Credentials getUserCredentials() throws IOException, GeneralSecurityException {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(new FileInputStream(this.CredentialsPath)));
		System.out.println(CredentialsPath);
		String clientId = clientSecrets.getDetails().getClientId();
		String clientSecret = clientSecrets.getDetails().getClientSecret();

		// 認証に利用するオブジェクトを作成
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets,
				ImmutableList.of("https://www.googleapis.com/auth/photoslibrary.readonly"))
						.setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR)).setAccessType("offline").build();

		// ローカルサーバを8080ポートで立てる
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(LOCAL_RECEIVER_PORT).build();

		Credential credential = flow.loadCredential("user");
		if (credential != null && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() == null
				|| credential.getExpiresInSeconds() > 60)) {
			// 認証成功した場合は認証情報を返す
			return UserCredentials.newBuilder().setClientId(clientId).setClientSecret(clientSecret)
					.setRefreshToken(credential.getRefreshToken()).build();
		}

		 // 手動で認証が必要な場合は、認証URLを取得する
		String redirectUri = receiver.getRedirectUri();
		System.out.println(redirectUri);
		AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);
		AuthUrl = authorizationUrl.build();
		return null;
	}
	
	
	// ここから先はGoogle Photos APIsを叩く処理
	
	//アルバムのリストを取得
	public List<Album> getAlbums() throws IOException, GeneralSecurityException {
		if (!init())
			return null;

		ListAlbumsRequest request = ListAlbumsRequest.getDefaultInstance();
		ListAlbumsResponse response = PhotosLibraryClientFactory.Client.listAlbumsCallable().call(request);
		return response.getAlbumsList();
	}
	
	// 画像を取得
	public Iterable<MediaItem> getPhotos() throws IOException, GeneralSecurityException {
		if(!init()) return null;
		
		Builder builder = SearchMediaItemsRequest.newBuilder();
		
		//指定したアルバムの画像を取得する
//		if()
		
		SearchMediaItemsRequest request = builder.build();
		SearchMediaItemsResponse response = PhotosLibraryClientFactory.Client.searchMediaItemsCallable().call(request);
		return response.getMediaItemsList();
	}

}