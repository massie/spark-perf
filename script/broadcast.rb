#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("broadcast")

  results = RDD_SLICES.product(BC_NUM_OBJECTS, BC_NUM_VARS, BC_OBJECT_LENGTH).map do |args|
    puts "TEST, RDD_SLICES, BC_NUM_OBJECTS, BC_NUM_VARS, BC_OBJECT_LENGTH, TIME"
    result = run("spark.perf.BroadcastTest", args)
    puts result
    STDOUT.flush
    [args, result]
  end

end