#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("logreg")

  puts "TEST, RDD_SLICES, LR_NUM_POINTS, LR_DIMENSION, TIME"
  results = RDD_SLICES.product(LR_NUM_POINTS, LR_DIMENSION).map do |args|
    result = run("spark.perf.LogReg", args)
    puts result
    STDOUT.flush
    [args, result]
  end
  
end

