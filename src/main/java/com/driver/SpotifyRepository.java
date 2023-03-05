package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {

         User user = new User(name,mobile);
         users.add(user);
         return user;

    }

    public Artist createArtist(String name) {

        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;

    }

    public Album createAlbum(String title, String artistName) {

        //creating new album and adding to the albums list
        Album album = new Album(title);
        albums.add(album);

        //search in artists list to check if the artist exists or not
        int flag = 1;
        for(Artist artist : artists){

            //if artist found then no need to create a new artist
            if(artist.getName().compareTo(artistName) == 0){
                flag = 0;
                List<Album> list = new ArrayList<>();
                //if artist already exists in the artistAlbumMap then add the album to the existing list otherwise add the artist
                if(artistAlbumMap.containsKey(artist)){

                    list = artistAlbumMap.get(artist);
                    list.add(album);
                    artistAlbumMap.put(artist,list);
                }else{
                    list.add(album);
                    artistAlbumMap.put(artist,list);
                }

                break;
            }

        }

        //if flag = 1 then create new artist
        if(flag == 1){
            Artist artist = new Artist(artistName);
            artists.add(artist);
            List<Album> list = new ArrayList<>();
            //if artist already exists in the artistAlbumMap then add the album to the existing list otherwise add the artist
            if(artistAlbumMap.containsKey(artist)){
                list = artistAlbumMap.get(artist);
                list.add(album);
                artistAlbumMap.put(artist,list);
            }else{
                list.add(album);
                artistAlbumMap.put(artist,list);
            }


        }


        return album;

    }

    public Song createSong(String title, String albumName, int length) throws Exception{

        Song song = new Song(title,length);
        int flag = 1;

        //search for the album
        //if album doesn't exist then throw exception

        for(Album album : albums){

            //album exists
            if(album.getTitle().compareTo(albumName) == 0){
                flag = 0;

                //create song
                List<Song> list = new ArrayList<>();
                songs.add(song);
                if(albumSongMap.containsKey(album)){
                    list = albumSongMap.get(album);
                    list.add(song);
                    albumSongMap.put(album,list);
                }else{
                    list.add(song);
                    albumSongMap.put(album,list);
                }

                break;
            }

        }

        //if the album didn't exist then throw exception
        if(flag == 1){
            throw new Exception("Album does not exist");
        }

        return song;

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        int flag = 1;
        Playlist playlist = new Playlist(title);
        //find if the user exists or not
        for(User user : users){

            //user exists
            if(user.getMobile().compareTo(mobile) == 0){

                flag = 0;
                playlists.add(playlist);

                List<Song> list = new ArrayList<>();
                //adding songs based on list
                for(Song song : songs){
                    if(song.getLength() == length)
                        list.add(song);
                }

                //adding new playlist to the map with list of songs of given length
                playlistSongMap.put(playlist,list);

                //adding the user to the playlistlistner map as the user also listens to the playlist
                List<User> t = new ArrayList<>();
                t.add(user);
                playlistListenerMap.put(playlist,t);

                //the user is the creator of the playlist
                creatorPlaylistMap.put(user,playlist);

                //user can listen to a lot of playlist hence map the list of playlist to user
                List<Playlist> l = new ArrayList<>();
                if(userPlaylistMap.containsKey(user)){
                    l = userPlaylistMap.get(user);
                    l.add(playlist);
                    userPlaylistMap.put(user,l);
                }else{
                    l.add(playlist);
                    userPlaylistMap.put(user,l);
                }


                break;
            }

        }

        //if user didn't exist throw exception
        if(flag == 1){
            throw new Exception("User does not exist");
        }

        return playlist;


    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

        int flag = 1;
        Playlist playlist = new Playlist(title);
        //find if the user exists or not
        for(User user : users){

            //user exists
            if(user.getMobile().compareTo(mobile) == 0){

                flag = 0;
                playlists.add(playlist);

                List<Song> list = new ArrayList<>();
                //adding songs based on list
                for(String songTitle : songTitles){

                    for(Song song : songs){

                        if(song.getTitle().compareTo(songTitle) == 0){
                            list.add(song);
                            break;
                        }

                    }

                }

                //adding new playlist to the map with list of songs of given songTitles
                playlistSongMap.put(playlist,list);

                //adding the user to the playlistlistner map as the user also listens to the playlist
                List<User> t = new ArrayList<>();
                t.add(user);
                playlistListenerMap.put(playlist,t);

                //the user is the creator of the playlist
                creatorPlaylistMap.put(user,playlist);

                //user can listen to a lot of playlist hence map the list of playlist to user
                List<Playlist> l = new ArrayList<>();
                if(userPlaylistMap.containsKey(user)){
                    l = userPlaylistMap.get(user);
                    l.add(playlist);
                    userPlaylistMap.put(user,l);
                }else{
                    l.add(playlist);
                    userPlaylistMap.put(user,l);
                }


                break;
            }

        }

        //if user didn't exist throw exception
        if(flag == 1){
            throw new Exception("User does not exist");
        }

        return playlist;




    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {


        int flagPlaylist = 1;
        Playlist playlist = new Playlist();
        //find playlist exists or not in playlist song map
        for(Playlist pl : playlists){

            if(pl.getTitle().compareTo(playlistTitle) == 0){
                flagPlaylist = 0;
                playlist = pl;
                break;
            }

        }

        if(flagPlaylist == 1){
            throw new Exception("Playlist does not exist");
        }

        //find user in the user list
        int flagUser= 1;

        User user = new User();
        for(User u : users){

            if(u.getMobile().compareTo(mobile) == 0){
                flagUser = 0;
                user = u;
                break;
            }

        }

        if(flagUser == 1){
            throw new Exception("User does not exist");
        }

        //check if user is creator or already  listener
        if(userPlaylistMap.containsKey(user)){

            List<Playlist> list = userPlaylistMap.get(user);

            for(Playlist pl : list){

                if(pl.getTitle().compareTo(playlistTitle) == 0){
                    return playlist;
                }

            }

        }

        //if the control reaches here it means the user didn't had this playlist
        //add user as listener
        //update user accordingly
        List<User> listUser = playlistListenerMap.get(playlist);
        listUser.add(user);
        playlistListenerMap.put(playlist,listUser);

        //if user is present but hasn't created any playlist
        //there might be a chance that he doesn't have any playlist
        if(userPlaylistMap.containsKey(user)){

            List<Playlist> listOfPlaylists = userPlaylistMap.get(user);
            listOfPlaylists.add(playlist);
            userPlaylistMap.put(user,listOfPlaylists);

        }else{

            List<Playlist> listOfPlayLists = new ArrayList<>();
            listOfPlayLists.add(playlist);
            userPlaylistMap.put(user,listOfPlayLists);

        }


        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

        Song song = new Song();
        User user = new User();
        //check if user exists
        int flagUser = 1;

        for(User u : users){

            if(u.getMobile().compareTo(mobile) == 0){
                flagUser = 0;
                user = u;
                break;
            }

        }

        //user doesn't exists
        if(flagUser == 1){
            throw new Exception("User does not exist");
        }

        int flagSong = 1;

        for(Song s : songs){

            if(s.getTitle().compareTo(songTitle) == 0){
                flagSong = 0;
                song = s;
                break;
            }

        }

        if(flagSong == 1){
            throw new Exception("Song does not exist");
        }

        //check if the song is there in the songLikeMap
        List<User> listUser= new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            listUser = songLikeMap.get(song);
            //if the user exists in the listUser list then don't like the song
            //as it is already liked by the user

            for(User u : listUser){
                //user has already liked the song
                if(u.getMobile().compareTo(mobile) == 0){
                    return song;
                }

            }

        }

        //increment the song like count
        //user has not liked the song before
        //increment the artist like count
        //map the song and user in the songLikeMap
        listUser.add(user);
        songLikeMap.put(song,listUser);
        song.setLikes(song.getLikes()+1);
        //increment artist like
        // find song in albumSongMap
        //if found then find the album in artistAlbumMap
        //if found then increment the arist

        for(Album album : albumSongMap.keySet()){
            List<Song> list = albumSongMap.get(album);

            for(Song s : list){

                //song found for a album
                if(s.getTitle().compareTo(songTitle) == 0){

                    //find artist
                    for(Artist artist : artistAlbumMap.keySet()){

                        List<Album> albums = artistAlbumMap.get(artist);

                        for(Album al : albums){

                            if(album.getTitle().compareTo(al.getTitle()) == 0){
                                artist.setLikes(artist.getLikes()+1);
                                return song;
                            }

                        }

                    }

                }

            }

        }

        return song;
    }

    public String mostPopularArtist() {

        int max = 0;
        Artist artist = new Artist();

        for(Artist a : artists){
            if(a.getLikes() >= max){
                max = a.getLikes();
                artist = a;
            }
        }

        return artist.getName();

    }

    public String mostPopularSong() {

        int max = 0;
        Song song = new Song();

        for(Song s : songs){
            if(s.getLikes() >= max){
                max = s.getLikes();
                song = s;
            }
        }

        return song.getTitle();

    }
}
