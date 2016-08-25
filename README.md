# step-count-master

1. Install `悦动圈` on your device, login with wechat, and bind with `微信运动`.
2. Shake your device, use your sniffer client to capture POST request to `http://report-segstep.51yund.com/sport/report_runner_info_step_batch`, when the App is uploading its step count.
3. Inspect the POST body, fill in `Config.java` with these fields.
![sniffer](http://7xrcar.com1.z0.glb.clouddn.com/sniffer.PNG)
4. Build your App, run.
![app](http://7xrcar.com1.z0.glb.clouddn.com/0825_4.jpg)
![wechat](http://7xrcar.com1.z0.glb.clouddn.com/0825_3.jpg)
