package template.music.data;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import template.music.model.MusicSong;
import template.music.model.Playlist;

public class GlobalVariable {

    private static GlobalVariable mInstance;

    public static synchronized GlobalVariable getInstance(Context context) {
        if (mInstance == null) mInstance = new GlobalVariable(context);
        return mInstance;
    }

    private MediaPlayer mp;

    // current playing music song
    private MusicSong musicSong;

    private OnMusicSongChange onMusicSongChange;
    private OnPlayerStateChange onPlayerStateChange;

    private List<Playlist> playlists = new ArrayList<>();

    public GlobalVariable(Context context) {
        mInstance = this;

        // Media Player
        mp = new MediaPlayer();

        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor afd = context.getAssets().openFd("short_music.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
        } catch (Exception e) {
            Toast.makeText(context, "Cannot load audio file", Toast.LENGTH_SHORT).show();
        }

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (onPlayerStateChange != null) onPlayerStateChange.onComplete();
            }
        });

        playlists = Constant.getPlaylist(context);
    }

    public MediaPlayer getMediaPlayer() {
        return mp;
    }

    public MusicSong getMusicSong() {
        return musicSong;
    }

    public boolean isPlaying() {
        try {
            return (mp != null && mp.isPlaying());
        } catch (Exception e) {
            return false;
        }
    }

    public void releasePlayer() {
        mp.release();
    }

    public void setPlayerState(PlayerState state) {
        try {
            if (state.equals(PlayerState.START)) {
                mp.start();
                if (onPlayerStateChange != null) onPlayerStateChange.onStart();
            } else if (state.equals(PlayerState.PAUSE)) {
                mp.pause();
                if (onPlayerStateChange != null) onPlayerStateChange.onPause();
            } else if (state.equals(PlayerState.RESTART)) {
                mp.seekTo(0);
                if (!mp.isPlaying()) {
                    setPlayerState(PlayerState.START);
                }
                if (onPlayerStateChange != null) onPlayerStateChange.onRestart();
            }
        } catch (Exception e) {

        }
    }

    public void setMusicSong(MusicSong musicSong) {
        this.musicSong = musicSong;
        if (onMusicSongChange != null) onMusicSongChange.onChange(this.musicSong);
    }

    public void setOnMusicSongChange(OnMusicSongChange onMusicSongChange) {
        this.onMusicSongChange = onMusicSongChange;
    }

    public void setOnPlayerStateChange(OnPlayerStateChange onPlayerStateChange) {
        this.onPlayerStateChange = onPlayerStateChange;
    }


    // setter getter playlist

    public List<Playlist> getPlaylist() {
        return playlists;
    }

    public Playlist addPlaylist(String name) {
        Playlist p = new Playlist();
        p.title = name;
        p.id = p.title.hashCode();
        this.playlists.add(p);
        return p;
    }

    public void removePlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
    }

    public Playlist updatePlaylist(Playlist playlist) {
        for (Playlist p : this.playlists) {
            if (p.id == playlist.id) {
                p.title = playlist.title;
                p.id = playlist.title.hashCode();
                playlist = p;
                break;
            }
        }
        return playlist;
    }

    public void clearPlaylist() {
        this.playlists.clear();
    }

    public boolean isPlaylistExist(String name) {
        Playlist p = new Playlist();
        p.title = name;
        p.id = p.title.hashCode();
        return this.playlists.contains(p);
    }
}
