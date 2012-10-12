### Spark Performance Testing Suite

1. Created a `config.rb` file in `/config`. There are several templates you can use.
2. Execute the `run.rb` file.

**Note**: Make sure to configure your log4j.properties file correctly to log out stderr, not stdout. All results will be printed to stdout and you don't want them to be intermingled with log messages.

### TODO

- Dynamically generate a spark-env.sh
- Broadcast Test
- Handle Spark crashes and freezes