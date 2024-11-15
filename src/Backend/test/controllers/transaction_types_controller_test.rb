require "test_helper"

class TransactionTypesControllerTest < ActionDispatch::IntegrationTest
  setup do
    @transaction_type = transaction_types(:one)
  end

  test "should get index" do
    get transaction_types_url, as: :json
    assert_response :success
  end

  test "should create transaction_type" do
    assert_difference("TransactionType.count") do
      post transaction_types_url, params: { transaction_type: { nome: @transaction_type.nome, user_id: @transaction_type.user_id } }, as: :json
    end

    assert_response :created
  end

  test "should show transaction_type" do
    get transaction_type_url(@transaction_type), as: :json
    assert_response :success
  end

  test "should update transaction_type" do
    patch transaction_type_url(@transaction_type), params: { transaction_type: { nome: @transaction_type.nome, user_id: @transaction_type.user_id } }, as: :json
    assert_response :success
  end

  test "should destroy transaction_type" do
    assert_difference("TransactionType.count", -1) do
      delete transaction_type_url(@transaction_type), as: :json
    end

    assert_response :no_content
  end
end
