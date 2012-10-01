#!/usr/bin/env ruby

# Group By
# ==================================================

numPairs = [100000, 1000000, 5000000, 10000000, 20000000, 50000000]
numKeys = [100, 1000, 10000]
numTasks = [10, 20, 100, 500]

args = numPairs.product(numKeys).product(numTasks).flatten.each_slice(3).to_a
args.select! do |arg|
  arg[1].to_i <= arg[0].to_i && arg[1].to_i >= arg[2].to_i
end

args = [[100000, 100, 10]] if ENV["ONETEST"] == "true"

args.each do |arg|
  print "GroupBy #{arg.join(" ")} "
  puts `./run spark.perf.TestGroupBy #{ENV["MASTER"]} #{arg.join(" ")}`
end

# Sort By
# ==================================================

args.each do |arg|
  print "SortBy #{arg.join(" ")} "
  puts `./run spark.perf.TestSortBy #{ENV["MASTER"]} #{arg.join(" ")}`
end


# Logistic Regression
# ==================================================

nList = [10000, 100000, 1000000, 2000000, 5000000, 10000000, 20000000]
dList = [10]
rList = [1]
numSlicesList = [10, 100, 1000]

args = nList.product(dList).product(rList).product(numSlicesList).flatten.each_slice(4).to_a

args = [[10000, 10, 1, 10]] if ENV["ONETEST"] == "true"

args.each do |arg|
  print "LR #{arg.join(" ")} "
  puts `./run spark.perf.TestLR #{ENV["MASTER"]} #{arg.join(" ")}`
end

# KMeans
# ==================================================

numPoints = [10000, 100000, 1000000, 5000000, 10000000, 20000000]
numTasks = [10, 100, 1000]

args = numPoints.product(numTasks).flatten.each_slice(2).to_a

args = [[1000, 10]] if ENV["ONETEST"] == "true"

args.each do |arg|
  print "KMeans #{arg.join(" ")} "
  puts `./run spark.perf.TestKMeans #{ENV["MASTER"]} #{arg.join(" ")}`
end