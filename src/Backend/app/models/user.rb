# == Schema Information
#
# Table name: users
#
#  id         :bigint           not null, primary key
#  nome       :string
#  sobrenome  :string
#  cpf        :string
#  endereco   :string
#  email      :string
#  senha      :string
#  created_at :datetime         not null
#  updated_at :datetime         not null
#
class User < ApplicationRecord
  has_many :transactions
  has_many :transaction_types
end
