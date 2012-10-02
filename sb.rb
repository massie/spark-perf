#!/usr/bin/env ruby

# Group By
# ==================================================

numPairs = [100000, 1000000, 5000000, 10000000, 20000000, 50000000, 100000000]
numKeys = [100, 1000, 10000]
numTasks = [10, 80, 500]

args = numPairs.product(numKeys).product(numTasks).flatten.each_slice(3).to_a
args = args.select do |arg|
  arg[1].to_i < arg[0].to_i && arg[1].to_i > arg[2].to_i
end

args = [[100000, 100, 10]] if ENV["ONETEST"] == "true"

args.each do |arg|
  print "SortBy #{arg.join(" ")} "
  STDOUT.flush
  puts `./run spark.perf.SortBy #{ENV["MASTER"]} #{arg.join(" ")}`
  STDOUT.flush
  sleep 5
end