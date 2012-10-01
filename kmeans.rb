#!/usr/bin/env ruby

numPoints = [10000, 100000, 1000000, 5000000, 10000000, 20000000]
numTasks = [10, 80, 1000]

args = numPoints.product(numTasks).flatten.each_slice(2).to_a

args = [[1000, 10]] if ENV["ONETEST"] == "true"

args.each do |arg|
  print "KMeans #{arg.join(" ")} "
  STDOUT.flush
  puts `./run spark.perf.TestKMeans #{ENV["MASTER"]} #{arg.join(" ")}`
  STDOUT.flush
  sleep 5
end