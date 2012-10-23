#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("kmeans")

  results = RDD_SLICES.product(KMEANS_NUM_POINTS, KMEANS_DIMENSION).map do |args|
    puts "TEST, RDD_SLICES, KMEANS_NUM_POINTS, KMEANS_DIMENSION, TIME"
    result = run("spark.perf.KMeans", args)
    puts result
    STDOUT.flush
    [args, result]
  end

end