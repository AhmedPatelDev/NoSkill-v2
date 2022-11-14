package me.ix.noskillv2.utils.lavaplayer;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter {

	public final AudioPlayer player;
	public final BlockingQueue<AudioTrack> queue;
	public final Guild guild;

	public TrackScheduler(AudioPlayer player, Guild guild) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.guild = guild;
	}

	public void queue(AudioTrack track) {
		if (!this.player.startTrack(track, true)) {
			this.queue.offer(track);
		}
	}

	public void nextTrack() {
		this.player.startTrack(this.queue.poll(), false);
	}
	
	public BlockingQueue<AudioTrack> getQueue() {
		return queue;
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			if (queue.isEmpty()) {
				TimerTask task = new TimerTask(){
				    public void run(){
				    	if(queue.isEmpty() && player.getPlayingTrack() == null) {
				    		PlayerManager.getInstance().play(guild, "https://www.youtube.com/watch?v=umqA5IMx_2I");
				    		
				    		TimerTask task = new TimerTask(){
							    public void run(){
							        AudioManager audioManager = guild.getAudioManager();
							        audioManager.closeAudioConnection(); 
							    }
							};
							Timer timer = new Timer();
							timer.schedule(task, 3*1000);
				    	}
				    }
				};
				Timer timer = new Timer();
				timer.schedule(task, 60*1000);
			} else {
				nextTrack();
			}
		}
	}
	
	
	/*
	 * 
	 * 
	 */
}
