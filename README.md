### Spark Performance Testing Suite

1. Created a `config.rb` file in `/config`. A template exists in `/config/config.rb.template`.
2. If on Amazon EC2, run warum.rb. This writes some large files in Spark's local temp directory to warm up the disks.
3. Run `run.rb [Git Commit Hash]`.

**Notes**: 
- Make sure to configure your log4j.properties file correctly to log out stderr, not stdout. All results will be printed to stdout and you don't want them to be intermingled with log messages.

### TODO
- Handle multiple configuration of JAVA_OPTS
- Broadcast Test
- Don't assume we can use ~/mesos-ec2/copy-dir to copy directories to slaves.
