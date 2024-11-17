# == Schema Information
#
# Table name: transaction_types
#
#  id         :bigint           not null, primary key
#  nome       :string
#  user_id    :bigint           not null
#  created_at :datetime         not null
#  updated_at :datetime         not null
#
require "test_helper"

class TransactionTypeTest < ActiveSupport::TestCase
  # test "the truth" do
  #   assert true
  # end
end
