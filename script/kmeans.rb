#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"

if TESTS.include?("kmeans")

  results = RDD_SLICES.product(NUM_REDUCE_TASKS, KMEANS_NUM_POINTS, KMEANS_DIMENSION).map do |args|
    result = `#{File.dirname(__FILE__) + "/run.rb spark.perf.KMeans " + args.join(" ")}`
    puts "KMeans,#{args.join(",")},#{result}"
    [args, result]
  end

end