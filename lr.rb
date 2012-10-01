#!/usr/bin/env ruby

nList = [10000, 100000, 1000000, 2000000, 5000000, 10000000, 20000000]
dList = [10]
rList = [1]
numSlicesList = [10, 100, 1000]

args = nList.product(dList).product(rList).product(numSlicesList).flatten.each_slice(4).to_a

args = [[10000, 10, 1, 10]] if ENV["ONETEST"] == "true"

args.each do |arg|
  print "LR #{arg.join(" ")} "
  STDOUT.flush
  puts `./run spark.perf.TestLR #{ENV["MASTER"]} #{arg.join(" ")}`
  STDOUT.flush
  sleep 5
end