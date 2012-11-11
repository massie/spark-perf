#!/usr/bin/env ruby

# Check if a config file exists
if !File.exists?("#{File.dirname(__FILE__)}/config/config.rb")
  $stderr.puts "Please create a #{File.dirname(__FILE__)}/config/config.rb file."
  exit 1
end

 # Warmup - write some data to Spark's tmp directory
require "#{File.dirname(__FILE__) + "/config/config.rb"}"
if JAVA_OPTS["spark.local.dir"]
  JAVA_OPTS["spark.local.dir"].split(",").map {|x| x.chomp}.each do |path|
    $stderr.puts "Warmup - Writing random data to #{path}."
    $stderr.puts `mkdir -p #{path}`
    $stderr.puts `dd if=/dev/urandom bs=#{1024*1024} count=500 | split -b #{1024*1024*100} - #{path}/random`
    $stderr.puts "Syncing to slaves, this may take a while."
    $stderr.puts `/root/mesos-ec2/copy-dir #{path}`
  end
else
  puts "spark.local.dir not set. Cannot perform disk warmup."
end